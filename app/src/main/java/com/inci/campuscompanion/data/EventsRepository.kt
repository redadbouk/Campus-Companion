package com.inci.campuscompanion.data

import com.inci.campuscompanion.data.remote.CampusApi
import com.inci.campuscompanion.data.remote.EventDto
import com.inci.campuscompanion.model.CampusEvent

class EventsRepository(private val api: CampusApi) {

    suspend fun getEvents(): List<CampusEvent> = api.getEvents().map { it.toCampusEvent() }
}

fun EventDto.toCampusEvent(): CampusEvent = CampusEvent(
    id = id,
    title = name,
    date = startingDate.take(16).replace("T", " "),
    location = address ?: "No location",
    description = description.orEmpty()
)
