import { service } from '@/utils/request'

export function findLoginLogsApi(queryInfo) {
  return service({
    url: '/pri/log/login',
    method: 'get',
    params: queryInfo
  })
}

export function deleteLoginLogByIdApi(id) {
  return service({
    url: '/pri/log/delete',
    method: 'delete',
    params: { id: id }
  })
}