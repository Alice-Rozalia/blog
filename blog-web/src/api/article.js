import { service } from '@/utils/request'

export function findArticlesApi(queryInfo) {
  return service({
    url: '/pub/article/list',
    method: 'get',
    params: queryInfo
  })
}

export function hotApi() {
  return service({
    url: '/pub/article/hot',
    method: 'get'
  })
}

export function findArticleByIdApi(id) {
  return service({
    url: '/pub/article/find_detail',
    method: 'get',
    params: { id: id }
  })
}

export function addArticleApi(form, tags) {
  return service({
    url: '/pri/article/add',
    method: 'post',
    data: form,
    params: { tags: tags }
  })
}

export function deleteArticleApi(id) {
  return service({
    url: '/pri/article/delete',
    method: 'delete',
    params: { id: id }
  })
}

export function editArticleApi(form, tags) {
  return service({
    url: '/pri/article/edit',
    method: 'put',
    data: form,
    params: { tags: tags }
  })
}