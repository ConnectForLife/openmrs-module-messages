
export interface ITemplateFieldValue {
  id: number | null;
  templateFieldId: number;
  value: string;
}

export const getDefaultValue = (): ITemplateFieldValue => ({
  id: null,
  templateFieldId: 0,
  value: ''
});
