
export interface ITemplateFieldValue {
  id: number | null;
  uuid: string | null;
  templateFieldId: number;
  value: string;
}

export const getDefaultValue = (): ITemplateFieldValue => ({
  id: null,
  uuid: null,
  templateFieldId: 0,
  value: ''
});
