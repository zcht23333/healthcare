import request from '@/utils/request'

export function getPharmaPolicyList(params) { return request({ url: '/pharmaPolicy/list', method: 'get', params }) }
export function addPharmaPolicy(data) { return request({ url: '/pharmaPolicy/add', method: 'post', data }) }
export function updatePharmaPolicy(data) { return request({ url: '/pharmaPolicy/update', method: 'post', data }) }
export function deletePharmaPolicy(id) { return request({ url: `/pharmaPolicy/delete/${id}`, method: 'post' }) }