package sn.ept.git.seminaire.cicd.data;


import sn.ept.git.seminaire.cicd.entities.Todo;


public final class TodoTestData extends TestData {

    public static Todo defaultTodo() {
        return Todo
                .builder()
                .id(Default.id)
                .createdDate(Default.createdDate)
                .lastModifiedDate(Default.lastModifiedDate)
                .version(Default.version)
                .title(Default.title)
                .description(Default.description)
                .tags(null)
                .build();
    }

}
