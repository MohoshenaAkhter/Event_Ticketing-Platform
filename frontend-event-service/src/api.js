const API_BASE_URL = "http://localhost:8080";

function getAuthHeader() {
  return localStorage.getItem("authHeader");
}

export function login(username, password) {
  const authHeader = "Basic " + btoa(`${username}:${password}`);
  localStorage.setItem("authHeader", authHeader);
}

export function logout() {
  localStorage.removeItem("authHeader");
}

async function request(path, options = {}) {
  const authHeader = getAuthHeader();

  const headers = {
    "Content-Type": "application/json",
    ...(options.headers || {})
  };

  if (authHeader) {
    headers.Authorization = authHeader;
  }

  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(`Request failed: ${response.status} ${response.statusText} ${errorText}`);
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}

// Event endpoints
export function getEvents() {
  return request("/events");
}

export function getEventById(id) {
  return request(`/events/${id}`);
}

export function createEvent(event) {
  return request("/events", {
    method: "POST",
    body: JSON.stringify(event)
  });
}

export function updateEvent(id, event) {
  return request(`/events/${id}`, {
    method: "PUT",
    body: JSON.stringify(event)
  });
}

export function assignVenueToEvent(eventId, venueId) {
  return request(`/events/${eventId}/venue?venueId=${venueId}`, {
    method: "PUT"
  });
}

export function updateEventStatus(eventId, status) {
  return request(`/events/${eventId}/status?status=${status}`, {
    method: "PATCH"
  });
}

// Venue endpoints
export function getVenues() {
  return request("/venues");
}

export function getVenueById(id) {
  return request(`/venues/${id}`);
}

export function createVenue(venue) {
  return request("/venues", {
    method: "POST",
    body: JSON.stringify(venue)
  });
}

export function updateVenue(id, venue) {
  return request(`/venues/${id}`, {
    method: "PUT",
    body: JSON.stringify(venue)
  });
}

export function deleteVenue(id) {
  return request(`/venues/${id}`, {
    method: "DELETE"
  });
}