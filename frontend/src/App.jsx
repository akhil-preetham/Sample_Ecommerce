import { NavLink, Route, Routes, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import { fetchProducts, login, registerUser } from "./lib/api";

const demoCredentials = {
  email: "demo@enterprise.local",
  password: "Admin@12345"
};

function App() {
  const [session, setSession] = useState(() => {
    const stored = localStorage.getItem("enterprise-session");
    return stored ? JSON.parse(stored) : null;
  });

  useEffect(() => {
    if (session) {
      localStorage.setItem("enterprise-session", JSON.stringify(session));
    } else {
      localStorage.removeItem("enterprise-session");
    }
  }, [session]);

  return (
    <div className="shell">
      <header className="topbar">
        <div>
          <p className="eyebrow">Enterprise Commerce</p>
          <h1>Operations-ready storefront</h1>
        </div>
        <nav className="nav">
          <NavLink to="/">Catalog</NavLink>
          <NavLink to="/login">Login</NavLink>
          <NavLink to="/register">Register</NavLink>
        </nav>
      </header>

      <main>
        <section className="hero">
          <div>
            <p className="pill">Gateway-connected React client</p>
            <h2>Shop the seeded demo catalog and validate authentication in one place.</h2>
            <p className="hero-copy">
              The frontend proxies through the API Gateway, reads the live product catalog,
              and signs users in against the user-service auth endpoints.
            </p>
          </div>
          <div className="hero-card">
            <span>Default demo login</span>
            <strong>{demoCredentials.email}</strong>
            <code>{demoCredentials.password}</code>
          </div>
        </section>

        <Routes>
          <Route
            path="/"
            element={<CatalogPage session={session} onLogout={() => setSession(null)} />}
          />
          <Route
            path="/login"
            element={<LoginPage onLogin={setSession} demoCredentials={demoCredentials} />}
          />
          <Route path="/register" element={<RegisterPage onRegister={setSession} />} />
        </Routes>
      </main>
    </div>
  );
}

function CatalogPage({ session, onLogout }) {
  const [query, setQuery] = useState("");
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let active = true;

    async function loadProducts() {
      setLoading(true);
      setError("");
      try {
        const payload = await fetchProducts(query.trim());
        if (active) {
          setProducts(payload?.data?.content || []);
        }
      } catch (err) {
        if (active) {
          setError(err.response?.data?.message || "Unable to load catalog from the gateway.");
          setProducts([]);
        }
      } finally {
        if (active) {
          setLoading(false);
        }
      }
    }

    const timeoutId = setTimeout(loadProducts, 250);
    return () => {
      active = false;
      clearTimeout(timeoutId);
    };
  }, [query]);

  return (
    <>
      <section className="panel toolbar">
        <div>
          <h3>Catalog</h3>
          <p>Search products served by `product-service` through the API Gateway.</p>
        </div>
        <div className="toolbar-actions">
          <input
            type="search"
            placeholder="Search the catalog"
            value={query}
            onChange={(event) => setQuery(event.target.value)}
          />
          {session ? (
            <button className="ghost" onClick={onLogout}>
              Sign out {session.firstName}
            </button>
          ) : (
            <span className="status-chip">Guest session</span>
          )}
        </div>
      </section>

      {session && (
        <section className="panel session-panel">
          <div>
            <h3>Authenticated</h3>
            <p>{session.email}</p>
          </div>
          <div>
            <span className="status-chip">Roles: {session.roles.join(", ")}</span>
          </div>
        </section>
      )}

      {error && <section className="panel error-banner">{error}</section>}

      <section className="catalog-grid">
        {loading ? (
          <article className="panel">Loading live catalog...</article>
        ) : products.length === 0 ? (
          <article className="panel">No products matched this query.</article>
        ) : (
          products.map((product) => (
            <article key={product.id} className="product-card">
              <div className="card-accent" />
              <div className="product-meta">
                <p className="product-sku">{product.sku}</p>
                <h3>{product.name}</h3>
                <p>{product.description}</p>
              </div>
              <div className="product-footer">
                <strong>${Number(product.basePrice).toFixed(2)}</strong>
                <span>
                  {product.reviewCount || 0} reviews · {product.rating?.toFixed(1) || "0.0"}★
                </span>
              </div>
            </article>
          ))
        )}
      </section>
    </>
  );
}

function LoginPage({ onLogin, demoCredentials }) {
  const navigate = useNavigate();
  const [form, setForm] = useState(demoCredentials);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  async function handleSubmit(event) {
    event.preventDefault();
    setSubmitting(true);
    setError("");

    try {
      const payload = await login(form);
      onLogin(payload.data);
      navigate("/");
    } catch (err) {
      setError(err.response?.data?.message || "Login failed.");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <AuthCard
      title="Sign in"
      subtitle="Authenticate against the user-service `/auth/login` endpoint."
      error={error}
    >
      <form className="auth-form" onSubmit={handleSubmit}>
        <label>
          Email
          <input
            type="email"
            value={form.email}
            onChange={(event) => setForm({ ...form, email: event.target.value })}
            required
          />
        </label>
        <label>
          Password
          <input
            type="password"
            value={form.password}
            onChange={(event) => setForm({ ...form, password: event.target.value })}
            required
          />
        </label>
        <button type="submit" disabled={submitting}>
          {submitting ? "Signing in..." : "Sign in"}
        </button>
      </form>
    </AuthCard>
  );
}

function RegisterPage({ onRegister }) {
  const navigate = useNavigate();
  const [form, setForm] = useState({
    email: "",
    password: "",
    firstName: "",
    lastName: "",
    phone: ""
  });
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState("");

  async function handleSubmit(event) {
    event.preventDefault();
    setSubmitting(true);
    setError("");

    try {
      const payload = await registerUser(form);
      onRegister(payload.data);
      navigate("/");
    } catch (err) {
      setError(err.response?.data?.message || "Registration failed.");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <AuthCard
      title="Create account"
      subtitle="Register a new shopper through the user-service `/auth/register` endpoint."
      error={error}
    >
      <form className="auth-form auth-grid" onSubmit={handleSubmit}>
        <label>
          First name
          <input
            type="text"
            value={form.firstName}
            onChange={(event) => setForm({ ...form, firstName: event.target.value })}
            required
          />
        </label>
        <label>
          Last name
          <input
            type="text"
            value={form.lastName}
            onChange={(event) => setForm({ ...form, lastName: event.target.value })}
            required
          />
        </label>
        <label className="full-span">
          Email
          <input
            type="email"
            value={form.email}
            onChange={(event) => setForm({ ...form, email: event.target.value })}
            required
          />
        </label>
        <label>
          Phone
          <input
            type="tel"
            value={form.phone}
            onChange={(event) => setForm({ ...form, phone: event.target.value })}
          />
        </label>
        <label>
          Password
          <input
            type="password"
            value={form.password}
            onChange={(event) => setForm({ ...form, password: event.target.value })}
            minLength="8"
            required
          />
        </label>
        <button type="submit" disabled={submitting} className="full-span">
          {submitting ? "Creating account..." : "Create account"}
        </button>
      </form>
    </AuthCard>
  );
}

function AuthCard({ title, subtitle, error, children }) {
  return (
    <section className="panel auth-card">
      <div className="auth-header">
        <h3>{title}</h3>
        <p>{subtitle}</p>
      </div>
      {error && <p className="error-text">{error}</p>}
      {children}
    </section>
  );
}

export default App;
