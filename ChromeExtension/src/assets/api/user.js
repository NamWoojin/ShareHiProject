import { createInstance } from './index.js'

const instance = createInstance()

function login(userObj, success, fail) {
  instance
  .post('login/basic', userObj)
  .then(success)
  .catch(fail);
}

export {login} 