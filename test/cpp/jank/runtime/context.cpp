#include <jank/runtime/context.hpp>
#include <jank/runtime/behavior/callable.hpp>

/* This must go last; doctest and glog both define CHECK and family. */
#include <doctest/doctest.h>

namespace jank::runtime
{
  TEST_CASE("Initialization")
  {
    context ctx;
    auto const locked_namespaces(ctx.namespaces.rlock());
    CHECK(locked_namespaces->find(jank::make_box<obj::symbol>("clojure.core")) != locked_namespaces->end());
    CHECK(locked_namespaces->find(jank::make_box<obj::symbol>("missing")) == locked_namespaces->end());
    CHECK(ctx.get_thread_state().current_ns->get_root()->as_ns()->name->equal(obj::symbol("", "clojure.core")));
  }

  TEST_CASE("Namespace changing")
  {
    context ctx;
    {
      auto const locked_namespaces(ctx.namespaces.rlock());
      CHECK(ctx.get_thread_state().current_ns->get_root()->as_ns()->name->equal(obj::symbol("", "clojure.core")));
      CHECK(locked_namespaces->find(jank::make_box<obj::symbol>("test")) == locked_namespaces->end());
    }
    ctx.get_thread_state().in_ns->get_root()->as_callable()->call(jank::make_box<obj::symbol>("test"));
    {
      auto const locked_namespaces(ctx.namespaces.rlock());
      CHECK(locked_namespaces->find(jank::make_box<obj::symbol>("test")) != locked_namespaces->end());
      CHECK(ctx.get_thread_state().current_ns->get_root()->as_ns()->name->equal(obj::symbol("", "test")));
    }
  }
}
