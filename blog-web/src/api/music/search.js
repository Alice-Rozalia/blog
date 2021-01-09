import {
    music
} from '@/utils/request'

export function searchMusic(key) {
    return music({
        url: '/search',
        method: 'get',
        params: {
            limit: 50,
            keywords: key
        }
    })
}