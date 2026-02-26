export default function Header({ onRefresh }) {
  return (
    <header className="flex items-center justify-between p-4 bg-gray-800 text-white">
      <h1 className="text-xl font-bold">Market Dashboard</h1>
      <button onClick={onRefresh} className="bg-blue-600 px-3 py-1 rounded hover:bg-blue-700">Refresh</button>
    </header>
  );
}