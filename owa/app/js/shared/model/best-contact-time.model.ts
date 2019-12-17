export interface IBestContactTime {
  personId: number | null,
  time: string | null
}

export const getDefaultValue = (): IBestContactTime => ({
  personId: null,
  time: null
});
