import { service } from '@/utils/request'

export function findUserListApi(queryInfo) {
  return service({
    url: '/pri/user/list',
    method: 'get',
    params: queryInfo
  })
}

export function userInfoApi() {
  return service({
    url: '/pri/user/info',
    method: 'get'
  })
}

export function sendCodeApi(phone, type) {
  return service({
    url: '/pub/user/code',
    method: 'get',
    params: {
      phone: phone,
      type: type
    }
  })
}

export function findMenuApi() {
  return service({
    url: '/pri/user/find_menu',
    method: 'get'
  })
}

export function userLoginApi(form) {
  return service({
    url: '/pub/user/login',
    method: 'post',
    data: form
  })
}

// 检验邮箱或手机号是否已被注册
export function checkApi(data, type) {
  return service({
    url: '/pub/user/check/' + data + '/' + type,
    method: 'get'
  })
}

export function userRegisterApi(form, code) {
  return service({
    url: '/pub/user/register',
    method: 'post',
    data: form,
    params: code
  })
}

// 修改密码
export function userChangePasswordApi(form, code) {
  return service({
    url: '/pub/user/change_password',
    method: 'put',
    data: form,
    params: code
  })
}

// 修改用户状态
export function userChangeStatusApi(id, status) {
  return service({
    url: '/pri/user/status',
    method: 'put',
    params: {
      id: id,
      status: status
    }
  })
}