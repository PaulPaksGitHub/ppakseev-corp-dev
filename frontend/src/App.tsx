import * as React from 'react'
import AddNoteForm from './modules/AddNoteForm';
import NoteItem from './modules/NoteItem';
import { Container, Grid, Typography } from '@material-ui/core';
import { useDataStream } from './core/hooks/useBehaviorSubject';
import NotesStore from './core/stores/NotesStore';
import {useEffect} from "react";

const App = () => {
  const notes = useDataStream(NotesStore.notesStream);

  const requestNotes = () => {
    NotesStore.requestNotes();
  }

  useEffect(() => {
    requestNotes();
  }, []);

  return <Container maxWidth={"md"}>
    <AddNoteForm />

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
}

export default App;
