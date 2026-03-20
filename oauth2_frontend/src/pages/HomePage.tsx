import { Container, Box, Typography, Button, Paper, Avatar } from "@mui/material"
import LogoutIcon from '@mui/icons-material/Logout';
import PersonIcon from '@mui/icons-material/Person';
import { useAuth } from '../store/authStore'
import { useNavigate } from "react-router-dom"

export default function HomePage() {

    const { user, logout } = useAuth();
    const navigate = useNavigate();

    const handleLogout = () : void => {
        logout();
        navigate('/login');
    }

    return(
        <Container maxWidth='sm'>
            <Box sx={{mt: 8}}>
                <Paper elevation={3} sx={{p: 4, textAlign: 'center'}}>
                    <Avatar sx={{ width:80, height: 80, mx: 'auto', bgcolor: 'primary.main'}}>
                        <PersonIcon fontSize="large" />
                    </Avatar>

                    <Typography variant="h5" fontWeight='bold' gutterBottom>
                        Welcome, {user?.name}!
                    </Typography>

                    <Box sx={{textAlign: 'left', bgcolor: 'grey.50', p: 2, borderRadius: 1, md: 3}}>
                        <Typography variant="body2" color='text.secondary'>이메일</Typography>
                        <Typography variant='body1' fontWeight='medium'>{user?.email}</Typography>
                        <Typography variant="body2" color='text.secondary'>권한</Typography>
                        <Typography variant='body1' fontWeight='medium'>{user?.role}</Typography>
                    </Box>

                    <Button
                        variant="outlined"
                        color='error'
                        startIcon={<LogoutIcon />}
                        onClick={handleLogout}
                        fullWidth>
                            Logout
                        </Button>
                </Paper>
            </Box>
        </Container>
    )
}