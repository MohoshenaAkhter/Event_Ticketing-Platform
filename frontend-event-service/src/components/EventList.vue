<script setup>
import { ref, onMounted } from 'vue'

const events = ref([])

onMounted(async () => {
  const response = await fetch('http://localhost:8083/events')

  events.value = await response.json()
})
</script>

<template>
  <div>
    <h1>Events</h1>

    <div
      v-for="event in events"
      :key="event.eventId"
      style="
        border:1px solid gray;
        margin:10px;
        padding:10px;
      "
    >
      <h2>{{ event.title }}</h2>

      <p>{{ event.description }}</p>

      <p>{{ event.category }}</p>

      <p>Status: {{ event.status }}</p>
    </div>
  </div>
</template>