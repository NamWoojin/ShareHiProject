import axios from 'axios'
const  API_BASE_URL = ``

function createInstance() {
  const instance = axios.create({
    baseURL: API_BASE_URL,
  });
  return instance;
}
export { createInstance, axiosTest }