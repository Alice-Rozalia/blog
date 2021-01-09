import { service } from '@/utils/request'

// 添加留言
export function addMessageApi(msg) {
  return service({
    url: '/pri/message/add',
    method: 'post',
    data: msg
  })
}

// 添加评论
export function addCommentApi(msg) {
  return service({
    url: '/pri/comment/add',
    method: 'post',
    data: msg
  })
}

// 查询留言列表（树形结构）
export function findMessagesApi(queryInfo) {
  return service({
    url: '/pub/message/list',
    method: 'get',
    params: queryInfo
  })
}

// 查询留言列表（无处理）
export function findMessageListApi(queryInfo) {
  return service({
    url: '/pri/message/primeval',
    method: 'get',
    params: queryInfo
  })
}

// 根据文章id查询评论列表
export function findCommentsByArticleIdApi(queryInfo) {
  return service({
    url: '/pub/comment/list_by_aid',
    method: 'get',
    params: queryInfo
  })
}

// 查询评论列表（无处理）
export function findCommentListApi(queryInfo) {
  return service({
    url: '/pri/comment/list',
    method: 'get',
    params: queryInfo
  })
}

// 根据 id 删除留言
export function deleteMessageByIdApi(id) {
  return service({
    url: '/pri/message/delete',
    method: 'delete',
    params: { id: id }
  })
}

// 根据 id 删除留言
export function deleteCommentByIdApi(id) {
  return service({
    url: '/pri/comment/delete',
    method: 'delete',
    params: { id: id }
  })
}

// 获取留言和评论的总数
export function findMessageAndCommnetApi() {
  return service({
    url: '/pub/cm/count',
    method: 'get'
  })
}