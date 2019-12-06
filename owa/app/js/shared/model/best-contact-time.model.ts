export interface IBestContactTime {
  patientTime?: Date,
  caregiverTime?: Date
}

export const getDefaultValue = (): IBestContactTime => ({
  patientTime: undefined,
  caregiverTime: undefined
});
