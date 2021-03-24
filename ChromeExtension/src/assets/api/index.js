import axios from 'axios'
const  API_BASE_URL = `https://j4f001.p.ssafy.io/api/`

function createInstance() {
  const instance = axios.create({
    baseURL: API_BASE_URL,
  });
  return instance;
}

export { createInstance }