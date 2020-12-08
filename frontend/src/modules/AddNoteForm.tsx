import * as React from 'react';
import {Box, Button, TextField} from '@material-ui/core';
import NotesStore from "../core/stores/NotesStore";
import {useState} from "react";

const AddNoteForm = () => {

  const [noteText, setNoteText] = useState("");

  const onPressAddNote = () => {
    setNoteText("");
    NotesStore.createNote({text: noteText});
  }

  return (
    <Box m={4} flexDirection="row">
      <TextField
        multiline
        id="standard-basic"
        label="Новая заметка..."
        value={noteText}
        onChange={e => {
          setNoteText(e.target.value)
        }}
      />
      <Box m={2}>
        <Button
          onClick={onPressAddNote}
          variant="contained"
          color={"primary"}
          disabled={noteText.trim().length === 0}
        >
          Добавить заметку
        </Button>
      </Box>
    </Box>
  )
}

export default AddNoteForm;
