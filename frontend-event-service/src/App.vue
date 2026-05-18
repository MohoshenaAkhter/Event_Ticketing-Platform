<script setup>
import { onMounted, ref } from 'vue';
import EventList from './components/EventList.vue';

const activeTab = ref('dashboard');

const venues = ref([]);
const loadingVenues = ref(false);

const venueForm = ref({
  name: '',
  location: '',
  capacity: ''
});

const selectedVenueId = ref('');

const navItems = [
  { key: 'dashboard', label: 'Dashboard' },
  { key: 'events', label: 'Event Management' },
  { key: 'venues', label: 'Venue Management' },
  { key: 'integration', label: 'Service Integration' }
];

async function fetchJson(url, options) {
  const response = await fetch(url, options);

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(`${url} returned HTTP ${response.status}: ${errorText}`);
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}

async function loadVenues() {
  loadingVenues.value = true;

  try {
    const payload = await fetchJson('http://localhost:8080/venues');
    venues.value = payload ?? [];
  } catch (error) {
    console.error('Failed to load venues', error);
    alert('Failed to load venues. Check backend or gateway.');
  } finally {
    loadingVenues.value = false;
  }
}

async function createVenue() {
  try {
    if (!venueForm.value.name || !venueForm.value.location || !venueForm.value.capacity) {
      alert('Please fill name, location, and capacity.');
      return;
    }

    const body = {
      name: venueForm.value.name,
      location: venueForm.value.location,
      capacity: Number(venueForm.value.capacity)
    };

    console.log('Creating venue with body:', body);

    const response = await fetch('http://localhost:8080/venues', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(body)
    });

    const text = await response.text();

    console.log('Create venue response status:', response.status);
    console.log('Create venue response body:', text);

    if (!response.ok) {
      alert(`Failed to create venue. Status: ${response.status}. Body: ${text}`);
      return;
    }

    venueForm.value = {
      name: '',
      location: '',
      capacity: ''
    };

    await loadVenues();

    alert('Venue created successfully.');
  } catch (error) {
    console.error('Create venue failed:', error);
    alert(`Failed to create venue: ${error.message}`);
  }
}

async function updateVenue() {
  try {
    if (!selectedVenueId.value) {
      alert('Enter venue ID for update.');
      return;
    }

    if (!venueForm.value.name || !venueForm.value.location || !venueForm.value.capacity) {
      alert('Please fill name, location, and capacity.');
      return;
    }

    await fetchJson(`http://localhost:8080/venues/${selectedVenueId.value}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        name: venueForm.value.name,
        location: venueForm.value.location,
        capacity: Number(venueForm.value.capacity)
      })
    });

    selectedVenueId.value = '';

    venueForm.value = {
      name: '',
      location: '',
      capacity: ''
    };

    await loadVenues();

    alert('Venue updated successfully.');
  } catch (error) {
    console.error('Failed to update venue', error);
    alert('Failed to update venue.');
  }
}

async function deleteVenue(id) {
  try {
    const confirmed = confirm(`Delete venue with ID ${id}?`);

    if (!confirmed) {
      return;
    }

    const response = await fetch(`http://localhost:8080/venues/${id}`, {
      method: 'DELETE'
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`Delete failed with HTTP ${response.status}: ${errorText}`);
    }

    await loadVenues();

    alert('Venue deleted successfully.');
  } catch (error) {
    console.error('Failed to delete venue', error);
    alert('Failed to delete venue.');
  }
}

function fillVenueForm(venue) {
  selectedVenueId.value = venue.id;

  venueForm.value = {
    name: venue.name,
    location: venue.location,
    capacity: venue.capacity
  };
}

onMounted(async () => {
  try {
    await loadVenues();
  } catch (error) {
    console.error(error);
  }
});
</script>

<template>
  <div class="app">

    <header class="appbar">
      <div>
        <strong>
          Tartu Tech Night 2026
        </strong>
      </div>
    </header>

    <!-- NAVIGATION -->

    <nav class="tabs" aria-label="Navigation">

      <button
        v-for="item in navItems"
        :key="item.key"
        type="button"
        :class="{ active: activeTab === item.key }"
        @click="activeTab = item.key"
      >
        {{ item.label }}
      </button>

    </nav>

    <!-- DASHBOARD -->

    <main
      v-if="activeTab === 'dashboard'"
      class="single-content"
    >

      <section class="bookings-panel">

        <div class="panel-title">
          <div>
            <h1>Event Ticketing Platform Dashboard</h1>

            <p>
              Enterprise System Integration Project
            </p>
          </div>
        </div>

        <div class="booking-card">

          <div>
            <strong>Event Service</strong>

            <span>
              Spring Boot Microservice
            </span>
          </div>

          <div>
            <small>Status</small>
            <b>Running</b>
          </div>

        </div>

        <div class="booking-card">

          <div>
            <strong>Venue Service</strong>

            <span>
              Spring Boot Microservice
            </span>
          </div>

          <div>
            <small>Status</small>
            <b>Running</b>
          </div>

        </div>

        <div class="booking-card">

          <div>
            <strong>API Gateway</strong>

            <span>
              Centralized Routing Layer
            </span>
          </div>

          <div>
            <small>Port</small>
            <b>8080</b>
          </div>

        </div>

        <div class="booking-card">

          <div>
            <strong>PostgreSQL</strong>

            <span>
              Persistent Database Storage
            </span>
          </div>

          <div>
            <small>Status</small>
            <b>Connected</b>
          </div>

        </div>

      </section>

    </main>

    <!-- EVENT MANAGEMENT -->

    <main
      v-else-if="activeTab === 'events'"
      class="single-content"
    >

      <section class="bookings-panel">

        <div class="panel-title">

          <div>
            <h1>Event Management</h1>

            <p>
              Manage all events through Event Service.
            </p>
          </div>

        </div>

        <EventList />

      </section>

    </main>

    <!-- VENUE MANAGEMENT -->

    <main
      v-else-if="activeTab === 'venues'"
      class="single-content"
    >

      <section class="bookings-panel">

        <div class="panel-title">

          <div>
            <h1>Venue Management</h1>

            <p>
              Loaded from Venue Service through API Gateway.
            </p>
          </div>

          <strong>{{ venues.length }}</strong>

        </div>

        <button
          class="refresh-button"
          type="button"
          :disabled="loadingVenues"
          @click="loadVenues"
        >
          {{ loadingVenues
            ? 'Loading...'
            : 'Refresh Venues'
          }}
        </button>

        <!-- CREATE / UPDATE VENUE FORM -->

        <div class="booking-card">

          <div>
            <strong>Create / Update Venue</strong>

            <span>
              Use this form to trigger POST /venues and PUT /venues/{id}.
            </span>
          </div>

        </div>

        <div class="venue-form">

          <input
            v-model="selectedVenueId"
            placeholder="Venue ID for update"
          />

          <input
            v-model="venueForm.name"
            placeholder="Venue name"
          />

          <input
            v-model="venueForm.location"
            placeholder="Location"
          />

          <input
            v-model="venueForm.capacity"
            placeholder="Capacity"
            type="number"
          />

          <button
            class="refresh-button"
            type="button"
            @click="createVenue"
          >
            Create Venue
          </button>

          <button
            class="refresh-button"
            type="button"
            @click="updateVenue"
          >
            Update Venue
          </button>

        </div>

        <div
          v-if="!venues.length"
          class="empty"
        >
          No venues found.
        </div>

        <div
          v-for="venue in venues"
          :key="venue.id"
          class="booking-card"
        >

          <div>

            <strong>
              {{ venue.name }}
            </strong>

            <span>
              ID: {{ venue.id }} | {{ venue.location }}
            </span>

          </div>

          <div>

            <small>Capacity</small>

            <b>
              {{ venue.capacity }}
            </b>

          </div>

          <div class="venue-actions">

            <button
              class="refresh-button"
              type="button"
              @click="fillVenueForm(venue)"
            >
              Edit
            </button>

            <button
              class="refresh-button"
              type="button"
              @click="deleteVenue(venue.id)"
            >
              Delete
            </button>

          </div>

        </div>

      </section>

    </main>

    <!-- SERVICE INTEGRATION -->

    <main
      v-else-if="activeTab === 'integration'"
      class="single-content"
    >

      <section class="bookings-panel">

        <div class="panel-title">

          <div>
            <h1>Service Integration</h1>

            <p>
              Event Service communicates with Venue Service through REST APIs.
            </p>
          </div>

        </div>

        <div class="booking-card">

          <div>
            <strong>Gateway Routing</strong>

            <span>
              API Gateway routes requests to backend services.
            </span>
          </div>

          <div>
            <small>Status</small>
            <b>Active</b>
          </div>

        </div>

        <div class="booking-card">

          <div>
            <strong>Event ↔ Venue Communication</strong>

            <span>
              REST integration using Spring Boot.
            </span>
          </div>

          <div>
            <small>Type</small>
            <b>Synchronous</b>
          </div>

        </div>

        <div class="booking-card">

          <div>
            <strong>Frontend Integration</strong>

            <span>
              Vue frontend connected through Gateway.
            </span>
          </div>

          <div>
            <small>Status</small>
            <b>Connected</b>
          </div>

        </div>

      </section>

    </main>

  </div>
</template>