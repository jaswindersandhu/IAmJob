package nick.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = SavedPosition.TABLE_NAME)
data class SavedPosition(
    @PrimaryKey @ColumnInfo(name = PositionColumnNames.COL_ID) val id: String,
    @ColumnInfo(name = PositionColumnNames.COL_TYPE) val type: String,
    @ColumnInfo(name = PositionColumnNames.COL_URL) val url: String,
    @ColumnInfo(name = PositionColumnNames.COL_CREATED_AT) val createdAt: String?,
    @ColumnInfo(name = PositionColumnNames.COL_COMPANY) val company: String,
    @ColumnInfo(name = PositionColumnNames.COL_COMPANY_URL) val companyUrl: String?,
    @ColumnInfo(name = PositionColumnNames.COL_LOCATION) val location: String,
    @ColumnInfo(name = PositionColumnNames.COL_TITLE) val title: String,
    @ColumnInfo(name = PositionColumnNames.COL_DESCRIPTION) val description: String,
    @ColumnInfo(name = PositionColumnNames.COL_HOW_TO_APPLY) val howToApply: String?,
    @ColumnInfo(name = PositionColumnNames.COL_COMPANY_LOGO) val companyLogo: String?,
    @ColumnInfo(name = PositionColumnNames.COL_IS_SAVED) val isSaved: Boolean,
    @ColumnInfo(name = PositionColumnNames.COL_HAS_APPLIED) val hasApplied: Boolean
) {

    @Ignore
    constructor(p: Position, isSaved: Boolean = false, hasApplied: Boolean = false) : this(
        id = p.id,
        type = p.type,
        url = p.url,
        createdAt = p.createdAt,
        company = p.company,
        companyUrl = p.companyUrl,
        location = p.location,
        title = p.title,
        description = p.description,
        howToApply = p.howToApply,
        companyLogo = p.companyLogo,
        isSaved = isSaved,
        hasApplied = hasApplied
    )

    @Ignore
    constructor(e: EphemeralPosition) : this(
        id = e.id,
        type = e.type,
        url = e.url,
        createdAt = e.createdAt,
        company = e.company,
        companyUrl = e.companyUrl,
        location = e.location,
        title = e.title,
        description = e.description,
        howToApply = e.howToApply,
        companyLogo = e.companyLogo,
        isSaved = e.isSaved,
        hasApplied = e.hasApplied
    )

    companion object {
        const val TABLE_NAME = "saved_positions"
    }
}