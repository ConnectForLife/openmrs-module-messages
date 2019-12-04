import { ReactFragment } from "react"

export class FormSection {
    constructor(title: string, subsections: Array<FormSubSection>) {
        this.title = title;
        this.subsections = subsections;
    }

    title: string
    subsections: Array<FormSubSection>
}

export class FormSubSection {
    constructor(name: string, body: ReactFragment, completed: boolean) {
        this.name = name;
        this.body = body;
        this.completed = completed;
    }

    name: string
    body: ReactFragment
    completed: boolean
}
