package com.inci.campuscompanion.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inci.campuscompanion.model.CampusEvent
import com.inci.campuscompanion.ui.theme.CampusCompanionTheme

@Composable
fun EventsScreen(
    state: EventsUiState,
    onRefresh: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) {
                Text("Back")
            }
            Text(
                text = "Campus Events",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            TextButton(
                onClick = onRefresh,
                enabled = state !is EventsUiState.Loading
            ) {
                Text("Refresh")
            }
        }

        HorizontalDivider()

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (state) {
                is EventsUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is EventsUiState.Content -> {
                    if (state.events.isEmpty()) {
                        Text(
                            text = "No events right now",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        EventList(events = state.events)
                    }
                }

                is EventsUiState.Error -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Couldn't load events",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onRefresh) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EventList(
    events: List<CampusEvent>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = events,
            key = { it.id }
        ) { event ->
            EventCard(event = event)
        }
    }
}

@Composable
fun EventCard(
    event: CampusEvent,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${event.date}  ·  ${event.location}",
                style = MaterialTheme.typography.bodyMedium
            )
            if (event.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = event.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventsPreview() {
    CampusCompanionTheme(darkTheme = false) {
        EventsScreen(
            state = EventsUiState.Content(
                listOf(
                    CampusEvent("1", "Event 1", "2026-10-20 12:23", "Main Hall", "Description 1"),
                    CampusEvent("2", "Event 2", "2026-10-21 09:00", "Lab B204", "Description 2")
                )
            ),
            onRefresh = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EventsErrorPreview() {
    CampusCompanionTheme(darkTheme = false) {
        EventsScreen(
            state = EventsUiState.Error("Unable to resolve host"),
            onRefresh = {},
            onBack = {}
        )
    }
}
