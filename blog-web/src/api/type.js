import { service } from '@/utils/request'

export function findCategoriesApi(queryInfo) {
  return service({
    url: '/pub/category/list',
    method: 'get',
    params: queryInfo
  })
}

export function findTagsApi(queryInfo) {
  return service({
    url: '/pub/tag/list',
    method: 'get',
    params: queryInfo
  })
}

export function deleteCategoryApi(id) {
  return service({
    url: '/pri/category/delete',
    method: 'delete',
    params: { id: id }
  })
}

export function deleteTagApi(id) {
  return service({
    url: '/pri/tag/delete',
    method: 'delete',
    params: { id: id }
  })
}

export function editCategoryApi(form) {
  return service({
    url: '/pri/category/edit',
    method: 'put',
    data: form
  })
}

export function editTagApi(form) {
  return service({
    url: '/pri/tag/edit',
    method: 'put',
    data: form
  })
}

export function addCategoryApi(form) {
  return service({
    url: '/pri/category/add',
    method: 'post',
    data: form
  })
}

export function addTagApi(form) {
  return service({
    url: '/pri/tag/add',
    method: 'post',
    data: form
  })
}