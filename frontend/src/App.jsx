import { useEffect, useState } from "react"
import axios from "axios"


function App() {
  const [data, setData] = useState([]);

  useEffect(() =>{
      const fetchData = async () =>{
        const response = await axios.get('http://localhost:8080/num');
        setData(response.data.number);
      }
      fetchData()
  }, [])



  return (
    <div className="App">
      <h1>Hello World {data}</h1>
    </div>
  )
}

export default App

