import { service } from '@/utils/request'

export function findFriendlyLinkApi(queryInfo) {
  return service({
    url: '/pub/link/list',
    method: 'get',
    params: queryInfo
  })
}

export function findResourceApi(queryInfo) {
  return service({
    url: '/pub/resource/list',
    method: 'get',
    params: queryInfo
  })
}

export function deleteFriendlyLinkApi(id) {
  return service({
    url: '/pri/link/delete',
    method: 'delete',
    params: { id: id }
  })
}

export function deleteResourceApi(id) {
  return service({
    url: '/pri/resource/delete',
    method: 'delete',
    params: { id: id }
  })
}

export function editFriendlyLinkApi(form) {
  return service({
    url: '/pri/link/edit',
    method: 'put',
    data: form
  })
}

export function editResourceApi(form) {
  return service({
    url: '/pri/resource/edit',
    method: 'put',
    data: form
  })
}

export function addFriendlyLink(form) {
  return service({
    url: '/pri/link/add',
    method: 'post',
    data: form
  })
}

export function addResource(form) {
  return service({
    url: '/pri/resource/add',
    method: 'post',
    data: form
  })
}