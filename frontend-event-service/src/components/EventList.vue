<script setup>
import { onMounted, ref } from 'vue';

const events = ref([]);
const loadingEvents = ref(false);

const eventForm = ref({
  title: '',
  description: '',
  category: '',
  organizerId: 1,
  startDateTime: '2026-05-20T18:00:00',
  endDateTime: '2026-05-20T22:00:00',
  status: 'DRAFT',
  posterUrl: ''
});

const assignEventId = ref('');
const assignVenueId = ref('');

function getAuthHeader() {
  return 'Basic ' + btoa('admin:admin123');
}

async function fetchJson(url, options = {}) {
  const response = await fetch(url, {
    ...options,
    headers: {
      ...(options.headers || {}),
      Authorization: getAuthHeader()
    }
  });

  const text = await response.text();

  if (!response.ok) {
    throw new Error(`${url} returned HTTP ${response.status}: ${text}`);
  }

  if (!text) {
    return null;
  }

  return JSON.parse(text);
}

async function loadEvents() {
  loadingEvents.value = true;

  try {
    const payload = await fetchJson('http://localhost:8080/events');
    events.value = payload ?? [];
  } catch (error) {
    console.error('Failed to load events', error);
    alert(`Failed to load events: ${error.message}`);
  } finally {
    loadingEvents.value = false;
  }
}

async function createEvent() {
  try {
    if (!eventForm.value.title || !eventForm.value.description || !eventForm.value.category) {
      alert('Please fill title, description, and category.');
      return;
    }

    const body = {
      title: eventForm.value.title,
      description: eventForm.value.description,
      category: eventForm.value.category,
      organizerId: Number(eventForm.value.organizerId),
      venueId: null,
      startDateTime: eventForm.value.startDateTime,
      endDateTime: eventForm.value.endDateTime,
      status: eventForm.value.status,
      posterUrl: eventForm.value.posterUrl
    };

    console.log('Creating event with body:', body);

    const createdEvent = await fetchJson('http://localhost:8080/events', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(body)
    });

    console.log('Created event:', createdEvent);

    await loadEvents();

    alert('Event created successfully.');
  } catch (error) {
    console.error('Failed to create event', error);
    alert(`Failed to create event: ${error.message}`);
  }
}

async function assignVenueToEvent() {
  try {
    if (!assignEventId.value || !assignVenueId.value) {
      alert('Enter event ID and venue ID.');
      return;
    }

    await fetchJson(
      `http://localhost:8080/events/${assignEventId.value}/venue?venueId=${assignVenueId.value}`,
      {
        method: 'PUT'
      }
    );

    await loadEvents();

    alert('Venue assigned successfully.');
  } catch (error) {
    console.error('Failed to assign venue', error);
    alert(`Failed to assign venue: ${error.message}`);
  }
}

onMounted(() => {
  loadEvents();
});
</script>

<template>
  <div>

    <button
      class="refresh-button"
      type="button"
      :disabled="loadingEvents"
      @click="loadEvents"
    >
      {{ loadingEvents ? 'Loading...' : 'Refresh Events' }}
    </button>

    <div class="booking-card">
      <div>
        <strong>Create Event</strong>
        <span>
          Triggers POST /events through the API Gateway.
        </span>
      </div>
    </div>

    <div class="venue-form">

      <input
        v-model="eventForm.title"
        placeholder="Title"
      />

      <input
        v-model="eventForm.description"
        placeholder="Description"
      />

      <input
        v-model="eventForm.category"
        placeholder="Category"
      />

      <input
        v-model="eventForm.organizerId"
        placeholder="Organizer ID"
        type="number"
      />

      <input
        v-model="eventForm.startDateTime"
        placeholder="Start Date Time"
      />

      <input
        v-model="eventForm.endDateTime"
        placeholder="End Date Time"
      />

      <input
        v-model="eventForm.status"
        placeholder="Status"
      />

      <input
        v-model="eventForm.posterUrl"
        placeholder="Poster URL"
      />

      <button
        class="refresh-button"
        type="button"
        @click="createEvent"
      >
        Create Event
      </button>

    </div>

    <div class="booking-card">
      <div>
        <strong>Assign Venue To Event</strong>
        <span>
          Triggers PUT /events/{eventId}/venue?venueId={venueId}. This demonstrates Event Service communicating with Venue Service.
        </span>
      </div>
    </div>

    <div class="venue-form">

      <input
        v-model="assignEventId"
        placeholder="Existing Event ID"
        type="number"
      />

      <input
        v-model="assignVenueId"
        placeholder="Existing Venue ID"
        type="number"
      />

      <button
        class="refresh-button"
        type="button"
        @click="assignVenueToEvent"
      >
        Assign Venue
      </button>

    </div>

    <div
      v-if="!events.length"
      class="empty"
    >
      No events found.
    </div>

    <div
      v-for="event in events"
      :key="event.eventId ?? event.id"
      class="booking-card"
    >

      <div>
        <strong>
          {{ event.title }}
        </strong>

        <span>
          ID: {{ event.eventId ?? event.id }} |
          {{ event.description }}
        </span>

        <span v-if="event.category">
          Category: {{ event.category }}
        </span>

        <span>
          Venue ID: {{ event.venueId ?? 'Not assigned yet' }}
        </span>
      </div>

      <div>
        <small>Status</small>
        <b>{{ event.status ?? 'N/A' }}</b>
      </div>

    </div>

  </div>
</template>