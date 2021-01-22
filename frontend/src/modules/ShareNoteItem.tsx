import * as React from 'react';
import {useState} from 'react';
import {Card, CardContent, Grid, Typography} from '@material-ui/core';
import {INote} from "../core/stores/interfaces/INote";

interface IProps extends INote {

}

const SharedNoteItem: React.FunctionComponent<IProps> = props => {
  const [displayedText, setDisplayedText] = useState(props.text || "");

  return (
    <Grid item xs={6}>
      <Card>
        <CardContent>
          <Typography>{displayedText}</Typography>
        </CardContent>
      </Card>
    </Grid>
  )
}

export default SharedNoteItem;
