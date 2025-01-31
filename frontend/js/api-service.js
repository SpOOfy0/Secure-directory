// Helper function to include JWT in headers
async function authenticatedFetch(url, options = {}) {
    const token = localStorage.getItem('jwtToken');
    const headers = options.headers || {};
    if (token) {
        headers['Authorization'] = `Bearer ${token}`;
    }
    headers['Content-Type'] = 'application/json';
    return fetch(url, { ...options, headers });
}

// Usage example:
async function someProtectedAction() {
    const response = await authenticatedFetch('http://localhost:8080/api/protected/endpoint', {
        method: 'GET',
    });
    // Handle response
}
