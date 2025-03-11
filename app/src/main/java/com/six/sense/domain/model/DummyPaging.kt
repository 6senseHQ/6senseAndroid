package com.six.sense.domain.model

/**
 * Represents a dummy data structure for paging examples or testing.
 *
 * This class holds basic information typically associated with a single item
 * in a paginated list. It's designed for scenarios where real data is not
 * necessary, and a simple representation is sufficient for demonstrating
 * paging concepts or UI behavior.
 *
 * @property id          A unique identifier for this item. Defaults to 0.
 * @property title       A short title or name for this item. Defaults to an empty string.
 * @property description A more detailed description of this item. Defaults to an empty string.
 */
data class DummyPaging(
    /**
     * Unique identifier for this item.
     */
    val id: Int = 0,
    /**
     * Short title or name for this item.
     */
    val title: String = "",
    /**
     * More detailed description of this item.
     */
    val description: String = ""
)
