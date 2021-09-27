
export interface IMessageDetails {
    id: number | null;
    templateFieldId: number;
    value: string;
  }
  
  export const getDefaultValue = (): IMessageDetails => ({
    id: null,
    templateFieldId: 0,
    value: ''
  });
  