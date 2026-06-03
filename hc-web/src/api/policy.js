import request from '@/utils/request'

export function getPolicyList(params) { return request({ url: '/policy/list', method: 'get', params }) }
export function addPolicy(data) { return request({ url: '/policy/add', method: 'post', data }) }
export function updatePolicy(data) { return request({ url: '/policy/update', method: 'post', data }) }
export function deletePolicy(id) { return request({ url: `/policy/delete/${id}`, method: 'post' }) }