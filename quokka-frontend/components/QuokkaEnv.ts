import {styled} from "@mui/material/styles";
import Paper from "@mui/material/Paper";

// export const BACKEND_HOST = "localhost";
export const BACKEND_HOST = "172.16.11.129";
export const BACKEND_SERVER_URL = "http://" + BACKEND_HOST + ":8080/quokka/api/v1";
// export const BACKEND_SERVER_URL = "https://" + BACKEND_HOST + ":8443/quokka/api/v1";

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