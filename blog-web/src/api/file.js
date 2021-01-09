import { service } from '@/utils/request'

export function deleteImgApi(file) {
  return service({
    url: '/pri/alioss/delete',
    method: 'delete',
    params: { file: file }
  })
}