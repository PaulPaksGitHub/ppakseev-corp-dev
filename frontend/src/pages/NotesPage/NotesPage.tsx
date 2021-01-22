import * as React from 'react';
import AddNoteForm from "../../modules/AddNoteForm";
import {Container, Grid, Typography} from "@material-ui/core";
import NoteItem from "../../modules/NoteItem";
import {useDataStream} from "../../core/hooks/useBehaviorSubject";
import NotesStore from "../../core/stores/NotesStore";
import {useEffect} from "react";
import SharedNoteItem from "../../modules/ShareNoteItem";

interface IProps {

}

const NotesPage: React.FunctionComponent<IProps> = props => {
  const notes = useDataStream(NotesStore.notesStream);
  const sharedNotes = useDataStream(NotesStore.sharedNotesStream);

  const requestNotes = () => {
    NotesStore.requestNotes();
  }

  useEffect(() => {
    requestNotes();
    NotesStore.requestSharedNotes();
  }, []);

  return (
    <Container maxWidth={"md"}>
      <Typography>Ваш ID: {localStorage.getItem("@user_id")}</Typography>

      <AddNoteForm/>

      {sharedNotes.data && sharedNotes.data.length > 0 ? (
        <div>
          <Typography>Общие заметки:</Typography>
          <Grid
            container
            direction="row"
            justify="center"
            alignItems="center"
            spacing={2}
          >
            {sharedNotes.data.map((noteItem, index) => (
              <SharedNoteItem
                {...noteItem}
                key={`s-${index}${noteItem.id}`}
              />
            ))}
          </Grid>
          <Typography>Личные заметки:</Typography>
        </div>
      ) : null}

      <Grid
        container
        direction="row"
        justify="center"
        alignItems="center"
        spacing={2}
      >
        {notes.data && notes.data.length > 0 ? notes.data.map((noteItem, index) => (
          <NoteItem
            {...noteItem}
            key={`${index}${noteItem.id}`}
          />
        )) : (
          <Typography>
            Список пуст, создайте новую заметку
          </Typography>
        )}
      </Grid>
    </Container>
  )
}

export default NotesPage;
