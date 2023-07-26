import * as React from 'react';
import {useEffect, useState} from 'react';
import Box from '@mui/material/Box';
import Stack from '@mui/material/Stack';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Container from '@mui/material/Container';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import {TransitionProps} from '@mui/material/transitions';
import Slide from '@mui/material/Slide';
import QuokkaAppHead from "../components/QuokkaAppHead";
import {BACKEND_SERVER_URL, currentDateTime, Item, scrollToTop} from "../components/QuokkaEnv"
import AlertDuplicatedDialog from "../components/AlertDuplicatedDialog";
import useMediaQuery from '@mui/material/useMediaQuery';
import {createTheme, ThemeProvider} from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import Checkbox from "@mui/material/Checkbox";
import FormControlLabel from "@mui/material/FormControlLabel";
import Link from 'next/link';
import Typography from '@mui/material/Typography';

// https://www.typescriptlang.org/static/TypeScript%20Types-ae199d69aeecf7d4a2704a528d0fd3f9.png
type Memo = {
    id: number;
    title: string;
    body: string;
    tags: string;
    pin: boolean;
    date: string;
};

const Transition = React.forwardRef(function Transition(
    props: TransitionProps & {
        children: React.ReactElement;
    },
    ref: React.Ref<unknown>,
) {
    return <Slide direction="up" ref={ref} {...props} />;
});

function AppHead() {
    // clock
    const [clock, setClock] = useState<string>();
    const [myAge, setMyAge] = useState<string>();
    setInterval(updateClock, 1000);

    function updateClock() {
        setClock(currentDateTime);
    }

    useEffect(() => {
        fetch(BACKEND_SERVER_URL + "/info/myage")
            .then(res => res.text())
            .then((myAge) => {
                console.log(myAge);
                    setMyAge(myAge);
                },
                // Note: it's important to handle errors here
                // instead of a catch() block so that we don't swallow
                // exceptions from actual bugs in components.
                error => {
                    console.log(error);
                }
            );
    }, [])

    return (
        <div>
            <QuokkaAppHead></QuokkaAppHead>
            <Typography component="h1" variant="h4">
                <Link href="https://github.com/tedwon/quokka" target="_blank">Quokka Memo App</Link>
            </Typography>
            <Typography component="h6" variant="h6">
                {clock}
            </Typography>
            <Typography component="h6" variant="h6">
                {myAge}
            </Typography>
        </div>
    );
}

function CreateMemo(props: {
    onCreate: (memo: Memo) => void,
}) {
    const [title, setTitle] = useState<string>('');
    const [body, setBody] = useState<string>('');
    const [tags, setTags] = useState<string>('');
    const [pin, setPin] = useState<boolean>(true);

    const handlePinChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setPin(event.target.checked);
    };

    // Dialog for creating a memo
    const [open, setOpen] = useState<boolean>(false);
    const handleClickOpen = () => {
        setOpen(true);
    };
    const handleClose = () => {
        setOpen(false);
    };

    // Dialog for duplicate key value violates
    const [open409, setOpen409] = useState<boolean>(false);
    const handleClickOpen409 = () => {
        setOpen409(true);
    };
    const handleClose409 = () => {
        setOpen409(false);
    };

    const handleCreate = () => {
        const newMemo = {
            'title': title,
            'body': body,
            'tags': tags,
            'pin': pin,
        };
        fetch(BACKEND_SERVER_URL + "/memo", {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(newMemo)
        })
            .then((response: Response) => {
                const code: number = response.status;
                response
                    .json()
                    .then((memo: Memo) => {
                            if (code === 201) {
                                props.onCreate(memo);
                                scrollToTop();
                                handleClose();
                            } else if (code === 409) {
                                handleClickOpen409();
                            }
                        },
                        error => {
                            console.log(error);
                        }
                    )
            })
    };

    return (
        <div>
            <h2>Create Memo</h2>
            <Button variant={"contained"} size={"small"} onClick={handleClickOpen}>Create</Button>
            {/*Full-screen dialogs*/}
            {/*https://mui.com/material-ui/react-dialog/#full-screen-dialogs*/}
            <Dialog
                fullScreen
                open={open}
                onClose={handleClose}
                TransitionComponent={Transition}
            >
                <DialogTitle>Create Memo</DialogTitle>
                <DialogContent>
                    <TextField
                        id="outlined-basic"
                        name="title"
                        label="Title"
                        variant="outlined"
                        fullWidth
                        defaultValue=""
                        onChange={event => setTitle(event.target.value)}
                        onMouseEnter={event => event.target}
                    />
                    <FormControlLabel control={<Checkbox checked={pin} onChange={handlePinChange}/>} label="Pin"/>
                    <p/>
                    <TextField
                        required
                        id="standard-multiline-static"
                        name="body"
                        label="Memo"
                        variant="outlined"
                        fullWidth
                        multiline
                        color="success"
                        rows={20}
                        defaultValue=""
                        autoFocus
                        onChange={event => setBody(event.target.value)}
                        onMouseEnter={event => event.target}
                    />
                    <p/>
                    <TextField
                        id="outlined-basic"
                        name="tags"
                        label="Tags"
                        variant="outlined"
                        fullWidth
                        defaultValue="eng,idea,"
                        onChange={event => setTags(event.target.value)}
                        onMouseEnter={event => event.target}
                    />
                </DialogContent>
                <DialogActions>
                    <Button type="submit" onClick={handleCreate}>Create</Button>
                    <Button type="submit" onClick={handleClose}>Cancel</Button>
                </DialogActions>
            </Dialog>
            <AlertDuplicatedDialog open={open409} onClose={handleClose409} body={body}/>
        </div>
    );
}

function SearchBox(props: {
    onClick: (memos: Memo[]) => void,
}) {
    const [pin, setPin] = useState<boolean>(false);
    const handlePinChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setPin(event.target.checked);
    };

    return (
        <article>
            <h2>Search&nbsp;&nbsp;<FormControlLabel control={<Checkbox checked={pin} onChange={handlePinChange}/>}
                                                    label="Pin"/></h2>
            <form onSubmit={(event: React.SyntheticEvent) => {
                event.preventDefault();
                const target = event.target as typeof event.target & {
                    search: { value: string };
                };
                const keyword: string = target.search.value; // typechecks!

                fetch(BACKEND_SERVER_URL + "/memo/pin/" + pin + "/" + keyword)
                    .then(res => res.json())
                    .then((memos: Memo[]) => {
                            props.onClick(memos);
                        },
                        error => {
                            console.log(error);
                        }
                    )
            }}>
                <TextField
                    id="outlined-basic"
                    name="search"
                    label="Search"
                    variant="outlined"
                    fullWidth
                    defaultValue=""
                    autoFocus
                    onMouseEnter={event => event.target}
                />
            </form>
        </article>
    );
}

function MemoTable(props: {
    memos: Memo[],
    onUpdate: (memo: Memo) => void,
    onDelete: (id: number) => void,
}) {
    // data from server
    const memos: Memo[] = props.memos;
    const [id, setId] = useState<number>(0);
    const [title, setTitle] = useState<string>('');
    const [body, setBody] = useState<string>('');
    const [tags, setTags] = useState<string>('');
    const [pin, setPin] = useState<boolean>(false);
    const [date, setDate] = useState<string>('');

    const handlePinChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        setPin(event.target.checked);
    };

    // Dialog for updating a memo
    const [open, setOpen] = useState<boolean>(false);
    const handleClickOpen = (memo: Memo) => {
        setId(memo.id);
        setTitle(memo.title);
        setBody(memo.body);
        setTags(memo.tags);
        setPin(memo.pin);
        setDate(memo.date);
        setOpen(true);
    };
    const handleClose = () => {
        setOpen(false);
    };

    // Dialog for duplicate key value violates
    const [open409, setOpen409] = useState<boolean>(false);
    const handleClickOpen409 = () => {
        setOpen409(true);
    };
    const handleClose409 = () => {
        setOpen409(false);
    };

    const handleUpdate = () => {
        const updatedMemoObj: Memo = {
            'id': id,
            'title': title,
            'body': body,
            'tags': tags,
            'pin': pin,
            'date': date
        };
        // Add/Create the new updated memo
        fetch(BACKEND_SERVER_URL + "/memo/" + id, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(updatedMemoObj)
        })
            .then((response: Response) => {
                const code: number = response.status;
                response
                    .json()
                    .then((memo: Memo) => {
                            if (code === 200) {
                                props.onUpdate(memo);
                                scrollToTop();
                                handleClose();
                            } else if (code === 409) {
                                handleClickOpen409();
                            }
                        },
                        error => {
                            console.log(error);
                        }
                    )
            })
    };

    const handleDelete = () => {
        // Remove
        fetch(BACKEND_SERVER_URL + "/memo/" + id, {
            method: 'DELETE',
            headers: {'Content-Type': 'application/json'},
        })
            .then((response: Response) => {
                    if (response.status === 204) {
                        props.onDelete(Number(id));
                    }
                    scrollToTop();
                    handleClose();
                },
                (error) => {
                    console.log(error);
                }
            )
    };

    const label = {inputProps: {'aria-label': 'Checkbox demo'}};


    return (
        <div>
            <h2>Memos</h2>
            <TableContainer component={Paper}>
                <Table sx={{minWidth: 650}} size="small" aria-label="a dense table">
                    <TableBody>
                        {memos.map((memo, idx) => (
                            <TableRow
                                key={idx}
                                sx={{'&:last-child td, &:last-child th': {fontSize: "0.8rem", border: 0}}}
                                onDoubleClick={() => {
                                    handleClickOpen(memo);
                                }}
                            >
                                <TableCell component="th" scope="row" variant="body">
                                    <b>{memo.pin === true ? <Checkbox {...label} disabled checked/> : ''}</b>
                                    <br/>
                                    <b>{memo.title}</b>
                                    <br/>
                                    {memo.body.split("\n").map((line, idx) => (
                                        <p key={idx}>{line}</p>
                                    ))}
                                    <br/>
                                    {memo.tags.length > 0 ? "Tags: " + memo.tags : null}
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
            {/*Full-screen dialogs*/}
            {/*https://mui.com/material-ui/react-dialog/#full-screen-dialogs*/}
            <Dialog
                fullScreen
                open={open}
                onClose={handleClose}
                TransitionComponent={Transition}
            >
                <DialogTitle>Update Memo</DialogTitle>
                <DialogContent>
                    <TextField
                        id="outlined-basic"
                        name="title"
                        label="Title"
                        variant="outlined"
                        fullWidth
                        value={title}
                        onChange={event => setTitle(event.target.value)}
                        onMouseEnter={event => event.target}
                    />
                    <FormControlLabel control={<Checkbox checked={pin} onChange={handlePinChange}/>} label="Pin"/>
                    <p/>
                    <TextField
                        required
                        id="standard-multiline-static"
                        name="body"
                        label="Memo"
                        variant="outlined"
                        fullWidth
                        multiline
                        color="success"
                        rows={20}
                        value={body}
                        autoFocus
                        onChange={event => setBody(event.target.value)}
                        onMouseEnter={event => event.target}
                    />
                    <p/>
                    <TextField
                        id="outlined-basic"
                        name="tags"
                        label="Tags"
                        variant="outlined"
                        fullWidth
                        value={tags}
                        onChange={event => setTags(event.target.value)}
                        onMouseEnter={event => event.target}
                    />
                </DialogContent>
                <DialogActions>
                    <Button type="submit" onClick={handleUpdate}>Update</Button>
                    <Button type="submit" onClick={handleDelete}>Delete</Button>
                    <Button type="submit" onClick={handleClose}>Cancel</Button>
                </DialogActions>
            </Dialog>
            <AlertDuplicatedDialog open={open409} onClose={handleClose409} body={body}/>
        </div>
    );
}

export default function Memorisation() {
    // Data
    const [memos, setMemos] = useState<Memo[]>([]);

    // Dark mode by System preference
    // https://mui.com/material-ui/customization/dark-mode/#system-preference
    const prefersDarkMode = useMediaQuery('(prefers-color-scheme: dark)');

    const theme = React.useMemo(
        () =>
            createTheme({
                palette: {
                    mode: prefersDarkMode ? 'dark' : 'light',
                },
            }),
        [prefersDarkMode],
    );

    // Retrieve All Memos from the backend-server
    // Note: the empty deps array [] means
    // this useEffect will run once
    // similar to componentDidMount()
    // https://reactjs.org/docs/faq-ajax.html
    useEffect(() => {
        fetch(BACKEND_SERVER_URL + "/memo")
            .then(res => res.json())
            .then((memos: Memo[]) => {
                    setMemos(memos);
                },
                // Note: it's important to handle errors here
                // instead of a catch() block so that we don't swallow
                // exceptions from actual bugs in components.
                error => {
                    console.log(error);
                }
            )
    }, [])

    return (
        <ThemeProvider theme={theme}>
            <CssBaseline/>
            <div>
                <Container>
                    <Box sx={{width: '100%'}}>
                        <Stack spacing={2}>
                            <Item>
                                <AppHead/>
                            </Item>
                            <Item>
                                <CreateMemo onCreate={(memo: Memo) => {
                                    const copyMemos = [...memos];
                                    copyMemos.splice(0, 0, memo);
                                    setMemos(copyMemos);
                                }}/>
                            </Item>
                            <Item>
                                <SearchBox onClick={(memos: Memo[]) => setMemos(memos)}/>
                            </Item>
                            <Item>
                                <MemoTable
                                    memos={memos}
                                    onUpdate={(memo: Memo) => {
                                        const copyMemos: Memo[] = [];
                                        copyMemos.push(memo);
                                        for (let i = 0; i < memos.length; i++) {
                                            if (memos[i].id !== memo.id) {
                                                copyMemos.push(memos[i]);
                                            }
                                        }
                                        setMemos(copyMemos);
                                    }}
                                    onDelete={(id: number) => {
                                        const copyMemos: Memo[] = [];
                                        for (let i = 0; i < memos.length; i++) {
                                            if (memos[i].id !== id) {
                                                copyMemos.push(memos[i]);
                                            }
                                        }
                                        setMemos(copyMemos);
                                    }}/>
                            </Item>
                        </Stack>
                    </Box>
                </Container>
            </div>
        </ThemeProvider>
    );
}