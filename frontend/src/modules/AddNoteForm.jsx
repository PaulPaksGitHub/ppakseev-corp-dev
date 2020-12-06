import React, { useState } from 'react'
import http from '../core/http';
import {Box, TextField, Button} from '@material-ui/core'

const AddNoteForm = props => {

  const [noteText, setNoteText] = useState(""); 

  const onPressAddNote = () => {
    setNoteText("");
    http.post('/note-create', {
      text: noteText,
    }).then(response => {
      props.onNoteCreated && props.onNoteCreated(response.data);
    })
  }

  return (
    <Box m={4} flexDirection="row">
      <TextField multiline id="standard-basic" label="Новая заметка..." value={noteText} onChange={e => {
        setNoteText(e.target.value)
      }} />
      <Box m={2}>
        <Button onClick={onPressAddNote} variant="contained" color={"primary"} disabled={noteText.trim().length === 0}>
          Добавить заметку
        </Button>
      </Box>
    </Box>
  )
}

export default AddNoteForm;