export interface IBestContactTime {
  patientTime?: string,
  caregiverTime?: string
}

export const getDefaultValue = (): IBestContactTime => ({
  patientTime: '00:00',
  caregiverTime: '00:00'
});
