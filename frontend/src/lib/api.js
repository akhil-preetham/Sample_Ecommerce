import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "/",
  timeout: 10000
});

export async function login(credentials) {
  const response = await api.post("/auth/login", credentials);
  return response.data;
}

export async function registerUser(payload) {
  const response = await api.post("/auth/register", payload);
  return response.data;
}

export async function fetchProducts(query) {
  const endpoint = query ? "/api/products/search" : "/api/products";
  const response = await api.get(endpoint, {
    params: query ? { q: query } : undefined
  });
  return response.data;
}

export default api;
