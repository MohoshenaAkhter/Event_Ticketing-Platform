<script setup>
import { onMounted, ref } from 'vue';
import EventList from './components/EventList.vue';

const activeTab = ref('dashboard');

const venues = ref([]);
const loadingVenues = ref(false);

const navItems = [
  { key: 'dashboard', label: 'Dashboard' },
  { key: 'events', label: 'Event Management' },
  { key: 'venues', label: 'Venue Management' },
  { key: 'integration', label: 'Service Integration' }
];

async function fetchJson(url, options) {
  const response = await fetch(url, options);

  if (!response.ok) {
    throw new Error(`${url} returned HTTP ${response.status}`);
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
  } finally {
    loadingVenues.value = false;
  }
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
              {{ venue.location }}
            </span>

          </div>

          <div>

            <small>Capacity</small>

            <b>
              {{ venue.capacity }}
            </b>

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