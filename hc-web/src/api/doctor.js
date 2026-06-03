import request from '@/utils/request'

export function getDoctorList(params) { return request({ url: '/doctor/list', method: 'get', params }) }
export function addDoctor(data) { return request({ url: '/doctor/add', method: 'post', data }) }
export function updateDoctor(data) { return request({ url: '/doctor/update', method: 'post', data }) }
export function deleteDoctor(id) { return request({ url: `/doctor/delete/${id}`, method: 'post' }) }
// 重置密码 API
export function resetDoctorPwd(id) { return request({ url: `/doctor/resetPwd/${id}`, method: 'post' }) }