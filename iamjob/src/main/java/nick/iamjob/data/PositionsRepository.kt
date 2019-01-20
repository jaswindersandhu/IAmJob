package nick.iamjob.data

import io.reactivex.Completable
import nick.data.dao.PositionsDao
import nick.data.model.Position
import nick.data.model.Search
import nick.networking.GitHubJobsService
import javax.inject.Inject

class PositionsRepository @Inject constructor(
    private val service: GitHubJobsService,
    private val positionsDao: PositionsDao
) {

    fun search(search: Search): Completable = with(search) {
        service.fetchPositions(
            description,
            location?.description,
            location?.latitude,
            location?.longitude,
            isFullTime,
            page
        ).flatMapCompletable { fetchedPositions ->
            // Mark all saved positions as stale -- we don't want them showing up in search results
            // if they're not part of the remotely fetched result set
            val savedPositions = positionsDao.querySavedFreshBlocking().map {
                it.copy(isFresh = false)
            }

            val hasViewedPositions = positionsDao.queryHasViewedFreshBlocking().map {
                it.copy(isFresh = false)
            }

            // Apply saved position states to the newly fetched positions
            val reconciledPositions = fetchedPositions.toMutableList().map { fetchedPosition ->
                val idMatchesFetchedPosition: (position: Position) -> Boolean = { position ->
                    position.id == fetchedPosition.id
                }

                val cachedPosition = savedPositions.find(idMatchesFetchedPosition)
                    ?: hasViewedPositions.find(idMatchesFetchedPosition)

                fetchedPosition.copy(
                    isSaved = cachedPosition?.isSaved == true,
                    hasApplied = cachedPosition?.hasApplied == true,
                    hasViewed = cachedPosition?.hasViewed == true,
                    isFresh = true
                )
            }

            // The insert part of this will also update any cached rows that were found in the fetched positions list
            deleteAllNonCachableThenInsert(reconciledPositions)
        }
    }

    private fun deleteAllNonCachableThenInsert(positions: List<Position>): Completable =
        Completable.fromAction { positionsDao.deleteAllNonCachableThenInsert(positions) }

    fun updatePosition(position: Position): Completable =
        Completable.fromAction { positionsDao.update(position) }

    fun querySavedPositions() = positionsDao.querySavedFresh()

    fun queryFreshPositions() = positionsDao.queryFresh()
}