import { service } from '@/utils/request'

export function addDiaryApi(content) {
  return service({
    url: '/pri/diary/add',
    method: 'post',
    params: { content: content }
  })
}

export function findDiarysApi(queryInfo) {
  return service({
    url: '/pub/diary/list',
    method: 'get',
    params: queryInfo
  })
}

export function findByIdApi(id) {
  return service({
    url: '/pri/diary/find_by_id',
    method: 'get',
    params: { id: id }
  })
}

export function deleteDiaryApi(id) {
  return service({
    url: '/pri/diary/delete',
    method: 'delete',
    params: { id: id }
  })
}

export function editDiaryApi(form) {
  return service({
    url: '/pri/diary/edit',
    method: 'put',
    data: form
  })
}