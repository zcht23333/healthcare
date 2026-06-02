import request from '@/utils/request'

export function getDrugList(params) { return request({ url: '/drugInfo/list', method: 'get', params }) }
export function addDrug(data) { return request({ url: '/drugInfo/add', method: 'post', data }) }
export function updateDrug(data) { return request({ url: '/drugInfo/update', method: 'post', data }) }
export function deleteDrug(id) { return request({ url: `/drugInfo/delete/${id}`, method: 'post' }) }