import React, { useEffect, useState } from 'react'
import http from './core/http';
import AddNoteForm from './modules/AddNoteForm';
import NoteItem from './modules/NoteItem';
import { Container, Grid, Typography } from '@material-ui/core';

const App = props => {
  const [notes, setNotes] = useState(undefined);

  const requestNotes = () => {
    http.get('notes').then(response => {
      setNotes(response.data);
    });
  }

  useEffect(() => {
    requestNotes();
  }, []);

  return <Container maxWidth={"md"}>
    <AddNoteForm
      onNoteCreated={(newNote) => {
        setNotes([...notes, newNote]);
      }}
    />

    <Grid
      container
      direction="row"
      justify="center"
      alignItems="center"
      spacing={2}
    >
      {notes && notes.length > 0 ? notes.map((noteItem, index) => (
        <NoteItem
          {...noteItem}
          key={`${index}${noteItem.id}`}
          index={index + 1}
          onNoteDeleted={() => {
            notes && setNotes(notes.filter(n => n.id !== noteItem.id));
          }}
          onNoteEdited={nextNote => {
            notes && setNotes(notes.map(prevNote => prevNote.id === noteItem.id ? nextNote : prevNote));
          }}
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
