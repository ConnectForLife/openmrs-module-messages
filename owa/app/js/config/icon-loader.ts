import {
  faHome,
  faChevronRight,
  faPencilAlt
} from '@fortawesome/free-solid-svg-icons';
import {
  faCheckCircle
} from '@fortawesome/free-regular-svg-icons';
import { library } from '@fortawesome/fontawesome-svg-core';

export const loadIcons = () => {
  library.add(
    faHome,
    faChevronRight,
    faCheckCircle,
    faPencilAlt
  );
};
