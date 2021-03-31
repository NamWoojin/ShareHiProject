import { createInstance } from './index.js'

const instance = createInstance()

function login(userObj, success, fail) {
  instance
  .post('login/basic', userObj)
  .then(success)
  .catch(fail);
}
function getOnlineDevice(mem_id, success, fail) {
  instance
  .get('device/getOnlineDevice', {  
    params: {
      mem_id,
    }
  })
  .then(success)
  .catch(fail)
} 
export {login,getOnlineDevice} 