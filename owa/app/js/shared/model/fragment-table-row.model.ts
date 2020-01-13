import { ReactFragment } from 'react';

export interface IFragmentRow {
  key: string;
  fragments: ReadonlyArray<ReactFragment>;
};
