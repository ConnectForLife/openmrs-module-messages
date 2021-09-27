export interface TemplateFieldDefaultValue {
  relationshipTypeId?: number;
  direction: string;
  templateFieldId?: number;
  defaultValue: string,
}

export const getDefaultValue = (): TemplateFieldDefaultValue => ({
  relationshipTypeId: undefined,
  direction: '',
  templateFieldId: undefined,
  defaultValue: ''
});

