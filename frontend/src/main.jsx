import React, { useState } from 'react';
import { createRoot } from 'react-dom/client';

function App() {
  const [employee, setEmployee] = useState({ name:'', email:'', phone:'', department:'', gender:'FEMALE' });
  const [ride, setRide] = useState({ employeeId:1, pickup:'', dropoff:'', departureTime:'', seatsNeeded:1, rideType:'OFFICE_COMMUTE', genderPreference:'ANY', smokingAllowed:false, notes:'' });
  const [matches, setMatches] = useState([]);

  const api = 'http://localhost:8080/api';
  const post = (path, data) => fetch(`${api}${path}`, { method:'POST', headers:{'Content-Type':'application/json'}, body: JSON.stringify(data)}).then(r=>r.json());

  return <div style={{fontFamily:'Arial', padding:20}}>
    <h1>Employee Ride Sharing</h1>
    <h3>Add Employee</h3>
    {['name','email','phone','department'].map(k => <input key={k} placeholder={k} value={employee[k]} onChange={e=>setEmployee({...employee,[k]:e.target.value})} style={{marginRight:8}}/>) }
    <select value={employee.gender} onChange={e=>setEmployee({...employee, gender:e.target.value})}><option>FEMALE</option><option>MALE</option><option>NON_BINARY</option><option>PREFER_NOT_TO_SAY</option></select>
    <button onClick={()=>post('/employees', employee)}>Create Employee</button>

    <h3>Create Ride Request</h3>
    <input placeholder='employeeId' type='number' value={ride.employeeId} onChange={e=>setRide({...ride, employeeId:Number(e.target.value)})} />
    <input placeholder='pickup' value={ride.pickup} onChange={e=>setRide({...ride,pickup:e.target.value})} />
    <input placeholder='dropoff' value={ride.dropoff} onChange={e=>setRide({...ride,dropoff:e.target.value})} />
    <input type='datetime-local' value={ride.departureTime} onChange={e=>setRide({...ride,departureTime:e.target.value})} />
    <input type='number' min='1' max='6' value={ride.seatsNeeded} onChange={e=>setRide({...ride,seatsNeeded:Number(e.target.value)})} />
    <select value={ride.rideType} onChange={e=>setRide({...ride,rideType:e.target.value})}><option>OFFICE_COMMUTE</option><option>AIRPORT</option><option>EVENT</option><option>EMERGENCY</option></select>
    <select value={ride.genderPreference} onChange={e=>setRide({...ride,genderPreference:e.target.value})}><option>ANY</option><option>FEMALE_ONLY</option><option>MALE_ONLY</option><option>SAME_GENDER</option></select>
    <label><input type='checkbox' checked={ride.smokingAllowed} onChange={e=>setRide({...ride,smokingAllowed:e.target.checked})}/>Smoking</label>
    <button onClick={()=>post('/rides',ride)}>Create Ride</button>

    <h3>Matches</h3>
    <button onClick={()=>fetch(`${api}/matches`).then(r=>r.json()).then(setMatches)}>Load Matches</button>
    <ul>{matches.map((m,i)=><li key={i}>Score {m.score}: {m.reasons.join(', ')}</li>)}</ul>
  </div>
}

createRoot(document.getElementById('root')).render(<App />);
