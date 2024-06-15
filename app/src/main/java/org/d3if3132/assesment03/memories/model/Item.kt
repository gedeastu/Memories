package org.d3if3132.assesment03.memories.model

import com.squareup.moshi.Json

data class Item(
    val id:Long,
    @Json(name = "title")val title:String,
    @Json(name = "description")val description:String,
    @Json(name = "imageId")val imageId:String,
)