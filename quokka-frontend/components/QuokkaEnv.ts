import {styled} from "@mui/material/styles";
import Paper from "@mui/material/Paper";

// export const BACKEND_SERVER_URL = "http://quokka:8080/quokka";
// export const BACKEND_SERVER_URL = "http://quokka-backend:8080/quokka";
export const BACKEND_SERVER_URL = "http://172.16.11.129:8080/quokka/api/v1";
// export const BACKEND_SERVER_URL = "http://127.0.0.1:8080/quokka";

export const scrollToTop = () => {
    window.scrollTo({
        top: 0,
        behavior: "smooth",
    });
};

export const Item = styled(Paper)(({theme}) => ({
    backgroundColor: theme.palette.mode === 'dark' ? '#1A2027' : '#fff',
    ...theme.typography.body2,
    padding: theme.spacing(1),
    textAlign: 'left',
    color: theme.palette.text.secondary,
}));

export function currentDateTime(): string {
    return new Date().toLocaleString('en-AU', {
        dateStyle: 'full',
        timeStyle: 'long',
    });
}