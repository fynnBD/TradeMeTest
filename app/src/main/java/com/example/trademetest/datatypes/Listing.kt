package com.example.trademetest.datatypes

data class Listing(val ListingId : Long,
        val Title : String?,
        val Category : String?,
        val Region : String?,
        val PictureHref : String?
    ) {
    override fun toString(): String {
        if (Title.isNullOrEmpty())
        {
            return ""
        }
        else
        {
            return Title
        }
    }
}
